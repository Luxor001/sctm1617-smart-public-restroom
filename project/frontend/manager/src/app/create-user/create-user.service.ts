import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LocalStorage } from '@ngx-pwa/local-storage';

@Injectable({
  providedIn: 'root'
})
export class CreateUserService {

  constructor(private http: HttpClient, private localStorage: LocalStorage) {}

  public createUser(fullname: string, username: string, password: string) {
    this.http.post('api/manager/register', {fullname: fullname, username: username, password: password}).subscribe(result => {
      let a = 0;
      debugger;
    });

  }
}
