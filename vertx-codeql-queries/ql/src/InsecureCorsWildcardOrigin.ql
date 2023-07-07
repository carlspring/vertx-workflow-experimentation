/**
 * @name Insecure CORS with wildcard origin
 * @description The CORS handler is configured to allow requests from any hosts.
 * @kind problem
 * @problem.severity critical
 * @id java/vertx/insecure-cors-wildcard
 * @tags security java/vertx
 */

import java
import semmle.code.java.StringFormat

class VertxCorsHandler extends RefType {
  VertxCorsHandler() {
    this.getASourceSupertype*().hasQualifiedName("io.vertx.ext.web.handler", "CorsHandler")
  }
}

class VertxCorsHandlerAddOriginMethodAccess extends MethodAccess {
  VertxCorsHandlerAddOriginMethodAccess() {
    exists(Method m |
      this.getMethod() = m and
      m.getName().matches("addOrigin") and
      m.getDeclaringType() instanceof VertxCorsHandler and
      this.getArgument(0).(StringLiteral).getValue() = "*"
    )
  }
}

from VertxCorsHandlerAddOriginMethodAccess call, Expr expr, StringFormatMethod format
where
  not call.getEnclosingCallable().getDeclaringType() instanceof VertxCorsHandler and
  not call.getLocation().getFile().getRelativePath().matches("%/src/test/%") and
  call.getArgument(format.getFormatStringIndex()) = expr
select
  call,
  "Insecure CORS configuration which allows wildcard origins."
