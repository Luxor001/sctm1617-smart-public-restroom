import { Component, OnInit } from '@angular/core';
import { LoginService } from '../login/login-service.service';
import { Router } from '@angular/router';
import { User } from '../code/User';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  user: User;
  constructor(private loginService: LoginService, private router: Router) { 
    this.user = loginService.user;
  }
  logout() {
    this.loginService.logout().subscribe(() => {
      this.router.navigate(['login']);
    })
  }
}