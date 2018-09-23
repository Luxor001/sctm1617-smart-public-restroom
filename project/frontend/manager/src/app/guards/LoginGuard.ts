import { CanActivate, Router } from '@angular/router';
import { LocalStorage } from '@ngx-pwa/local-storage';
import { Injectable } from '@angular/core';
import {map} from 'rxjs/operators';
import { Observable } from '../../../node_modules/rxjs';

/**
 * Una guard che previene l'accesso alla pagina /login qualora un utente abbia già un loginToken nel proprio indexedDB.
 */
@Injectable()
export class LoginGuard implements CanActivate {
    constructor(private localStorage: LocalStorage, private router: Router) {}

    canActivate(): Observable<boolean> {
      return this.localStorage.getItem('loginToken').pipe(map(value => {
          if(value != null)          
            this.router.navigate(['restrooms']);
          return value == null;
      }));
    }
  }
