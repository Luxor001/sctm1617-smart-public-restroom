import { Component, OnInit } from '@angular/core';
import { LoginService } from '../login/login-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private loginService: LoginService, private router: Router) { }

  ngOnInit() {
  }

  logout() {
    this.loginService.logout().subscribe(() => {      
      this.router.navigate(['login']);
    })
  }

}
