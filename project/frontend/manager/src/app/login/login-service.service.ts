import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LocalStorage } from '@ngx-pwa/local-storage';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  
  constructor(private http: HttpClient, private localStorage: LocalStorage) { }

  public login(): boolean {

    // aggiungiamo il token di login all'applicazione...
    this.localStorage.setItemSubscribe('loginToken', "iopadsfjnfaopisnfaspionafsponafs");
    return true;
    /*this.http.post('api/login', {}).subscribe(result => {

    })*/
  }
  
  public logout() {

  }
}
