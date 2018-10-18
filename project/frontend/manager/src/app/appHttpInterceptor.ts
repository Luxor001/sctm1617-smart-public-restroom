import { Injectable, Inject } from "@angular/core";
import { HttpInterceptor, HttpHandler, HttpEvent, HttpRequest } from "@angular/common/http";
import { Observable } from "rxjs";
import { LocalStorage } from "@ngx-pwa/local-storage";
import { map, concatMap } from "rxjs/operators";

@Injectable()
export class AppHttpInterceptor implements HttpInterceptor {
  constructor(@Inject('BASE_URL') private baseUrl: string, private localStorage: LocalStorage) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    return this.localStorage.getItem('loginToken').pipe(concatMap(loginToken => {
        const headers = loginToken != null ? req.headers.append('loginToken', loginToken) : req.headers;
        
        req = req.clone({
          url: this.baseUrl + req.url,
          headers: headers
        });
        return next.handle(req);
    }));
  }
}
