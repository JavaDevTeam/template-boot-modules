package me.wuwenbin.modules.utils.web;

import me.wuwenbin.modules.utils.http.R;
import me.wuwenbin.modules.utils.util.function.TemplateConsumer;
import me.wuwenbin.modules.utils.util.function.TemplateSupplier;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;


/**
 * Controller层经常要做的操作，这里把基本操作给函数式化了
 * 方便同意调用，省的重复写try catch代码段
 * created by Wuwenbin on 2017/12/20 at 下午3:28
 *
 * @author wuwenbin
 * @since 1.10.5.RELEASE
 */
public final class Controllers {

    private String operationName;

    private String successMsg;
    private String errorMsg;
    private String exceptionMsg;

    private Controllers(String operationName) {
        this.operationName = operationName;
    }

    private Controllers(String successMsg, String errorMsg, String exceptionMsg) {
        this.successMsg = successMsg;
        this.errorMsg = errorMsg;
        this.exceptionMsg = exceptionMsg;
    }

    public static Controllers builder(String operationName) {
        return new Controllers(operationName);
    }

    public static Controllers builder(String successMsg, String errorMsg, String exceptionMsg) {
        return new Controllers(successMsg, errorMsg, exceptionMsg);
    }

    /**
     * 带操作之前判断的，判断为真则执行主要内容
     *
     * @param preOperate
     * @param mainBody
     * @param elseSupplier
     * @return
     */
    public R exec(Supplier<Boolean> preOperate, TemplateSupplier<Boolean> mainBody, Supplier<R> elseSupplier) {
        if (preOperate.get()) {
            return exec(mainBody);
        } else {
            return elseSupplier.get();
        }
    }

    /**
     * 带操作之前判断的，判断为真则执行主要内容，mainBody可以带额外的操作的（例如在执行成功之后给R在put额外的参数）
     *
     * @param preOperate
     * @param mainBody
     * @param elseSupplier
     * @param successOperate
     * @param failureOperate
     * @param exceptionOperate
     * @return
     */
    public R exec(Supplier<Boolean> preOperate, TemplateSupplier<Boolean> mainBody, Supplier<R> elseSupplier,
                  UnaryOperator<R> successOperate, UnaryOperator<R> failureOperate, UnaryOperator<R> exceptionOperate) {
        if (preOperate.get()) {
            return exec(mainBody, successOperate, failureOperate, exceptionOperate);
        } else {
            return elseSupplier.get();
        }
    }

    /**
     * try catch且带有if条件判断的
     *
     * @param ifCondition
     * @return
     */
    public R exec(TemplateSupplier<Boolean> ifCondition) {
        try {
            boolean res = ifCondition.get();
            if (res) {
                String msg = operationName == null ? successMsg : operationName.concat("成功！");
                return R.ok(msg);
            } else {
                String msg = operationName == null ? errorMsg : operationName.concat("失败！");
                return R.error(msg);
            }
        } catch (Exception e) {
            String msg = operationName == null ? exceptionMsg : operationName.concat("异常，原因：").concat(e.getMessage());
            return R.error(msg);
        }
    }

    /**
     * try catch且带有if条件判断的，成功/失败/异常之后可以给R添加额外参数的
     *
     * @param ifCondition
     * @return
     */
    public R exec(TemplateSupplier<Boolean> ifCondition, UnaryOperator<R> successOperate, UnaryOperator<R> failureOperate, UnaryOperator<R> exceptionOperate) {
        try {
            boolean res = ifCondition.get();
            if (res) {
                String msg = operationName == null ? successMsg : operationName.concat("成功！");
                R r = R.ok(msg);
                return successOperate.apply(r);
            } else {
                String msg = operationName == null ? errorMsg : operationName.concat("失败！");
                R r = R.error(msg);
                return failureOperate.apply(r);
            }
        } catch (Exception e) {
            String msg = operationName == null ? exceptionMsg : operationName.concat("异常，原因：").concat(e.getMessage());
            R r = R.error(msg);
            return exceptionOperate.apply(r);
        }
    }

    /**
     * try catch且带有if条件判断的，成功之后可以给R添加额外参数的
     *
     * @param ifCondition
     * @param successOperate
     * @return
     */
    public R execWrapSuccess(TemplateSupplier<Boolean> ifCondition, UnaryOperator<R> successOperate) {
        return exec(ifCondition, successOperate, UnaryOperator.identity(), UnaryOperator.identity());
    }

    /**
     * try catch且带有if条件判断的，失败之后可以给R添加额外参数的
     *
     * @param ifCondition
     * @param failureOperate
     * @return
     */
    public R execWrapFailure(TemplateSupplier<Boolean> ifCondition, UnaryOperator<R> failureOperate) {
        return exec(ifCondition, UnaryOperator.identity(), failureOperate, UnaryOperator.identity());
    }

    /**
     * try catch且带有if条件判断的，异常之后可以给R添加额外参数的
     *
     * @param ifCondition
     * @param exceptionOperate
     * @return
     */
    public R execWrapException(TemplateSupplier<Boolean> ifCondition, UnaryOperator<R> exceptionOperate) {
        return exec(ifCondition, UnaryOperator.identity(), UnaryOperator.identity(), exceptionOperate);
    }

    /**
     * try catch且带有if条件判断的，成功或失败之后可以给R添加额外参数的
     *
     * @param ifCondition
     * @param successOperate
     * @param failureOperate
     * @return
     */
    public R execWrapSuccessFailure(TemplateSupplier<Boolean> ifCondition, UnaryOperator<R> successOperate, UnaryOperator<R> failureOperate) {
        return exec(ifCondition, successOperate, failureOperate, UnaryOperator.identity());
    }

    /**
     * try catch且带有if条件判断的，成功或异常之后可以给R添加额外参数的
     *
     * @param ifCondition
     * @param successOperate
     * @param exceptionOperate
     * @return
     */
    public R execWrapSuccessException(TemplateSupplier<Boolean> ifCondition, UnaryOperator<R> successOperate, UnaryOperator<R> exceptionOperate) {
        return exec(ifCondition, successOperate, UnaryOperator.identity(), exceptionOperate);
    }

    /**
     * try catch且带有if条件判断的，失败或异常之后可以给R添加额外参数的
     *
     * @param ifCondition
     * @param failureOperate
     * @param exceptionOperate
     * @return
     */
    public R execWrapFailureException(TemplateSupplier<Boolean> ifCondition, UnaryOperator<R> failureOperate, UnaryOperator<R> exceptionOperate) {
        return exec(ifCondition, UnaryOperator.identity(), failureOperate, exceptionOperate);
    }


    /**
     * 无需要判断的，仅需要try catch即可
     *
     * @param <T>
     * @param t
     * @param tryOperate
     * @return
     */
    public <T> R execLight(T t, TemplateConsumer<T> tryOperate) {
        return execLight(t, tryOperate, UnaryOperator.identity(), UnaryOperator.identity());
    }

    /**
     * 无需要判断的，仅需要try catch即可，获取结果之后可以给R赋予额外的参数
     *
     * @param <T>
     * @param t
     * @param tryOperate
     * @return
     */
    public <T> R execLight(T t, TemplateConsumer<T> tryOperate, UnaryOperator<R> tryResultOperate, UnaryOperator<R> exceptionOperate) {
        try {
            tryOperate.accept(t);
            String msg = operationName == null ? successMsg : operationName.concat("成功！");
            R r = R.ok(msg);
            return tryResultOperate.apply(r);
        } catch (Exception e) {
            String msg = operationName == null ? exceptionMsg : operationName.concat("异常，原因：").concat(e.getMessage());
            R r = R.error(msg);
            return exceptionOperate.apply(r);
        }
    }
}