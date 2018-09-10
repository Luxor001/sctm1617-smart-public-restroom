import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { CreateUserService } from './create-user.service';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.scss']
})
export class CreateUserComponent {

  constructor(private createUserService: CreateUserService) { }

  register(form: NgForm) {
    this.createUserService.createUser(form.value.fullname, form.value.username, form.value.password)
  }

}
