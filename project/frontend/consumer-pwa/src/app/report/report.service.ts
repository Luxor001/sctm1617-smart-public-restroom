import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Report } from './report';
import { concatMap } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(private http: HttpClient) { }

  sendReport(reportData: Report) {
    return this.http.post("api/data/sendreport", reportData)
      .pipe(concatMap((result: any) => {
        return of(result.result);
      }));
  }
}
