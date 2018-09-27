import { CanActivate, Router } from "@angular/router";
import { LocalStorage } from "@ngx-pwa/local-storage";
import { Injectable } from "@angular/core";
import { map, concatMap } from "rxjs/operators";
import { LoginService } from "../login/login-service.service";
import { of } from "rxjs";

@Injectable()
export class AuthGuard implements CanActivate {
    constructor(private localStorage: LocalStorage, private loginService: LoginService, private router: Router) {

    }

    // Controlliamo se esiste un loginToken dentro l'applicazione. Se esiste, l'utente si è loggato.
    canActivate() {
        if (!this.loginService.user == null)
            return true;

        return this.localStorage.getItem('loginToken').pipe(concatMap(loginToken => {
            if (loginToken == null) {
                this.router.navigate(['/login']);
                return of(false);
            }
            return this.loginService.loginByToken(loginToken);
        }));
    }
}