import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { LoginService } from './login-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  constructor(private loginService: LoginService, private router: Router) {}

  login(form: NgForm) {
    let loginResult = this.loginService.login();
    if(loginResult){
      this.router.navigate(['home']);
    }
  }
}
