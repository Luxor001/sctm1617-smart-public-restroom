import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CreateRestroomService {
  constructor(private http: HttpClient) { }

  public createRestroom(newRestroom: any) {
    return this.http.post('api/manager/addRestroom', newRestroom)
      .pipe(map((result: any) => {
        return result.result;
      }));
  }
}
