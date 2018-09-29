import { Component, OnInit } from '@angular/core';
import { ReportService } from './report.service';
import { Report } from '../code/report';

@Component({
  selector: 'app-consumer-reports',
  templateUrl: './consumer-reports.component.html',
  styleUrls: ['./consumer-reports.scss']
})
export class ConsumerReportsComponent {
  reports: Report[];
  displayedColumns: string[] = ['name', 'Timestamp', 'comment'];
  constructor(private reportService: ReportService) {
    this.reportService.getReports().subscribe((result: any) => {
      if(result.result) 
        this.reports = result.reports;     
    });
   }
}
