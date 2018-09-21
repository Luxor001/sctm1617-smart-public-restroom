import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { FormGroup, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-create-restroom',
  templateUrl: './create-restroom.component.html',
  styleUrls: ['./create-restroom.component.scss']
})
export class CreateRestroomComponent {
  newRestroom = {
    id: this.generateGuid(), 
    address: null,
    cityAddress: null,
    company: null
  };

  constructor(public dialogRef: MatDialogRef<CreateRestroomComponent>) {
  }

  generateGuid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
      var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
      return v.toString(16);
    });
  }

}
