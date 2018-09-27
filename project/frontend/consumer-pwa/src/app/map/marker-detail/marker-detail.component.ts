import { Component, OnInit, Input } from '@angular/core';
import { RestRoom } from '../../code/restRoom';

@Component({
  selector: 'app-marker-detail',
  templateUrl: './marker-detail.component.html',
  styleUrls: ['./marker-detail.component.scss']
})
export class MarkerDetailComponent implements OnInit {
  @Input() restRoom: RestRoom;
  constructor() { }

  ngOnInit() {
  }

  public avaiableToilets(): number {
    if(this.restRoom.sensorData != null)
      return this.restRoom.sensorData.roomsInfo.filter(restRoom => restRoom.closed).length;
  }
}
