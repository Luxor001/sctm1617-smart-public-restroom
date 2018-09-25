import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LocalStorage } from '@ngx-pwa/local-storage';
import { concatMap, map } from 'rxjs/operators';
import { of } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class CreateUserService {
  constructor(private http: HttpClient, private localStorage: LocalStorage) { }
 
  public createUser(fullname: string, username: string, password: string) {
    return this.http.post('api/manager/register', { fullname: fullname, username: username, password: password })
    .pipe(concatMap((result: any) => {
      return of(result.result);
    }));
  }  
}