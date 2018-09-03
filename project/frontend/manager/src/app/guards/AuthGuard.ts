import { CanActivate } from "@angular/router";
import { LocalStorage } from "@ngx-pwa/local-storage";
import { Injectable } from "@angular/core";

@Injectable()
export class AuthGuard implements CanActivate {
    constructor(private localStorage: LocalStorage){

    }

    // Controlliamo se esiste un loginToken dentro l'applicazione. Se esiste, l'utente si è loggato.
    canActivate() {
        this.localStorage.getItem('loginToken').subscribe(value => {
            let a = value;
        })
      return true;
    }
  }