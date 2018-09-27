import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Form } from '@angular/forms';
import { ReportService } from './report.service';
import { Report } from './report';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss'],
  providers: [ReportService]
})
export class ReportComponent {

  @ViewChild("fullname") private fullnameInput: ElementRef;
  constructor(private reportService: ReportService, public snackBar: MatSnackBar) { }

  onSubmit(form: any) {
    this.reportService.sendReport(new Report(form.value.name, form.value.comment))
    .subscribe(success => {
      if (success) {
        this.snackBar.open(`Report sent, thanks for you valuable feedback.`, null, { panelClass: 'userCreatedSnackbar', duration: 3000 });
        form.reset();
        for (let name in form.controls)
          form.controls[name].setErrors(null);          
        this.fullnameInput.nativeElement.focus();
      }
    });
  }
}
