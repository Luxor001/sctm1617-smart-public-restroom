import { Injectable, Inject } from "@angular/core";
import { HttpInterceptor, HttpHandler, HttpEvent, HttpRequest } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable()
export class AppHttpInterceptor implements HttpInterceptor {
  constructor(@Inject('BASE_URL') private baseUrl: string) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    req = req.clone({
      url: this.baseUrl + req.url
    });
    return next.handle(req);
  }
}
