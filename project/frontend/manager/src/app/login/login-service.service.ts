import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LocalStorage } from '@ngx-pwa/local-storage';
import { map, concatMap } from '../../../node_modules/rxjs/operators';
import { Observable, timer, of } from '../../../node_modules/rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  constructor(private http: HttpClient, private localStorage: LocalStorage) {}

  public login(username: string, password: string, gruppoAziendale: number): Observable<boolean> {
    /*this.http.post('api/login', {}).subscribe(result => {

    });*/
    return this.doFakeLogin();
  }

  private doFakeLogin() {
    return of({loginResult: true}).pipe(concatMap(result => {
      if (result.loginResult) {
        const dummyToken = '9fd9b877fed59334b81dada9bd978c4a';
        return this.localStorage.setItem('loginToken', dummyToken).pipe(map( () => true));
      }
      else
        return of(false);
    }));
  }

  public logout() {
    return this.localStorage.removeItem('loginToken');
  }
}
