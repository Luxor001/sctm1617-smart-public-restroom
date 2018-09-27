import { Component, OnInit } from '@angular/core';
import { ReportService } from './report.service';
import { Report } from '../code/report';

@Component({
  selector: 'app-consumer-reports',
  templateUrl: './consumer-reports.component.html',
  styles: ['consumer-restrooms.scss']
})
export class ConsumerReportsComponent {
  reports: Report[];
  constructor(private reportService: ReportService) {
    this.reportService.getReports().subscribe((result: any) => {
      if(result.result) 
        this.reports = result.reports;     
    });
   }
}
