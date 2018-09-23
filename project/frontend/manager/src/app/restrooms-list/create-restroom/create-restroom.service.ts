import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CreateRestroomService {
  constructor(private http: HttpClient) {}

  public createRestroom(newRestroom: any) {
    this.http.post('api/manager/addRestroom', newRestroom).subscribe(result => {
      let a = 0;
      debugger;
    });

  }
}
