import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LocalStorage } from '@ngx-pwa/local-storage';
import { map, concatMap } from '../../../node_modules/rxjs/operators';
import { Observable, timer, of } from '../../../node_modules/rxjs';
import { User } from '../code/User';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  user: User;
  constructor(private http: HttpClient, private localStorage: LocalStorage) { }

  public login(username: string, password: string, gruppoAziendale: number): Observable<boolean> {
    return this.http.post('api/manager/login', { username: username, password: password })
      .pipe(concatMap((result: any) => {
        if (result.result) {
          this.user = result.user;
          return this.localStorage.setItem('loginToken', result.loginToken).pipe(map(() => true));
        }
        else
          return of(false);
      }));
  }

  public loginByToken(loginToken: string): Observable<boolean> {
    return this.http.post('api/manager/login', { loginToken: loginToken })
      .pipe(map((result: any) => {
        if(result.result)
        this.user = result.user;
        return result.result;
      }));
  }

  public logout() {
    this.user = null;
    return this.localStorage.removeItem('loginToken');
  }
}
