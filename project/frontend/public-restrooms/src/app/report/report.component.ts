import { Component, OnInit } from '@angular/core';
import { Form } from '@angular/forms';
import { ReportService } from './report.service';
import { Report } from './report';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss'],
  providers: [ReportService]
})
export class ReportComponent implements OnInit {

  constructor(private reportService: ReportService) { }

  ngOnInit() {
  }

  onSubmit(form: any) {
    this.reportService.sendReport(new Report(form.value.name, form.value.comment));    
  }
}
