import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LocalStorage } from '@ngx-pwa/local-storage';
import { map, concatMap } from '../../../node_modules/rxjs/operators';
import { Observable, timer, of } from '../../../node_modules/rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  constructor(private http: HttpClient, private localStorage: LocalStorage) { }

  public login(username: string, password: string, gruppoAziendale: number): Observable<boolean> {
    return this.http.post('api/manager/login', { username: username, password: password })
      .pipe(concatMap((result: any) => {
        if (result.result) {
          const dummyToken = '9fd9b877fed59334b81dada9bd978c4a';
          return this.localStorage.setItem('loginToken', dummyToken).pipe(map(() => true));
        }
        else
          return of(false);
      }));
  }

  public logout() {
    return this.localStorage.removeItem('loginToken');
  }
}
