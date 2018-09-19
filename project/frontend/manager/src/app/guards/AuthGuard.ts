import { CanActivate, Router } from "@angular/router";
import { LocalStorage } from "@ngx-pwa/local-storage";
import { Injectable } from "@angular/core";
import { map } from "rxjs/operators";
import { LoginService } from "../login/login-service.service";

@Injectable()
export class AuthGuard implements CanActivate {
    constructor(private localStorage: LocalStorage, private loginService: LoginService, private router: Router) {

    }

    // Controlliamo se esiste un loginToken dentro l'applicazione. Se esiste, l'utente si è loggato.
    canActivate() {
        return this.localStorage.getItem('loginToken').pipe(map(loginToken => {
            if (loginToken == null) {
                this.router.navigate(['/login']);
                return false;
            }
            return this.loginService.loginByToken(loginToken);
        }));
    }
}