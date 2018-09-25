import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { CreateUserService } from './create-user.service';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.scss']
})
export class CreateUserComponent {

  constructor(private createUserService: CreateUserService, public snackBar: MatSnackBar) { }

  register(form: NgForm) {
    this.createUserService.createUser(form.value.fullname, form.value.username, form.value.password).subscribe(success => {
      if (success)
        this.snackBar.open(`User "${form.value.username}" created.`, "Ok", { panelClass: 'userCreatedSnackbar', duration: 3000 });
      else
        this.snackBar.open(`Username "${form.value.username}" already in use.`, "Dismiss", { panelClass: 'userNotCreatedSnackbar', duration: 3000 });
    });
  }

}
