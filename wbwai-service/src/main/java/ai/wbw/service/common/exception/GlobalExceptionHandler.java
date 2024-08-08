package ai.wbw.service.common.exception;

import ai.wbw.service.common.model.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @Description 全局异常处理类
 * @Version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 全局异常
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultEntity<String> handleException(HttpServletRequest req, Exception e) throws Exception {
        ResultEntity r = new ResultEntity();
        r.setCode(HttpStatus.BAD_REQUEST.value());
        r.setMsg(e.getMessage());
        r.setData(e.getCause());
        logger.error(e.getMessage(),e);
        return r;
    }
}