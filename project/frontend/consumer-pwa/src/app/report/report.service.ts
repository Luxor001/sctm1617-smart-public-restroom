import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Report } from './report';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(private http: HttpClient) { }

  sendReport(reportData: Report) {
    this.http.post("api/data/sendreport", reportData).subscribe();
  }
}
