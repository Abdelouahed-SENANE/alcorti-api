package ma.youcode.api.advice;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.starter.utilities.exceptions.AbstractGlobalExceptionHandler;

@RestControllerAdvice
public class AdviceController extends AbstractGlobalExceptionHandler {
}
