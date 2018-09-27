import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { LoginService } from './login-service.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  constructor(private loginService: LoginService, private router: Router, public snackBar: MatSnackBar) {}

  login(form: NgForm) {
    // TODO: inserire debounce del submit dell'utente
    this.loginService.login(form.value.username, form.value.password, form.value.gruppoAziendale)
      .subscribe(success => {
        if (success)
          this.router.navigate(['']);
        else
          this.snackBar.open("Wrong username or password", null, {panelClass: 'loginSnackbar', duration: 3000 });
    });
  }
}