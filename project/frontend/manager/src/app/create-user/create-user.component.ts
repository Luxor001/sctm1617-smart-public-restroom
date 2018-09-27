import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { CreateUserService } from './create-user.service';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.scss']
})
export class CreateUserComponent {

  @ViewChild("fullname") private fullnameInput: ElementRef;
  constructor(private createUserService: CreateUserService, public snackBar: MatSnackBar) { }

  register(form: NgForm) {
    this.createUserService.createUser(form.value.fullname, form.value.username, form.value.password).subscribe(success => {
      if (success) {
        this.snackBar.open(`User "${form.value.username}" created.`, null, { panelClass: 'userCreatedSnackbar', duration: 3000 });
        form.reset();
        for (let name in form.controls)
          form.controls[name].setErrors(null);          
        this.fullnameInput.nativeElement.focus();
      }
      else
        this.snackBar.open(`Username "${form.value.username}" already in use.`, null, { panelClass: 'userNotCreatedSnackbar', duration: 3000 });
    });
  }

}
