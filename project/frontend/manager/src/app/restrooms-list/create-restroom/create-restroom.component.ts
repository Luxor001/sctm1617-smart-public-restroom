import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { FormGroup, FormBuilder, NgForm } from '@angular/forms';
import { CreateRestroomService } from './create-restroom.service';

@Component({
  selector: 'app-create-restroom',
  templateUrl: './create-restroom.component.html',
  styleUrls: ['./create-restroom.component.scss']
})
export class CreateRestroomComponent {
  guid = this.generateGuid();
  constructor(public dialogRef: MatDialogRef<CreateRestroomComponent>, private createRestroomService: CreateRestroomService) {
  }

  generateGuid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
      var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
      return v.toString(16);
    });
  }

  closeDialog(result: NgForm) {
    let newRestroom = result.value;
    newRestroom.guid = this.guid;

    this.createRestroomService.createRestroom(newRestroom).subscribe(value => {
      this.dialogRef.close(newRestroom);
    });    
  }
}
