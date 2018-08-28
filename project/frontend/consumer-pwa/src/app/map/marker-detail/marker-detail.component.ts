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
    return this.restRoom.restRoomsInfo.filter(restRoom => restRoom.closed).length;
  }
}
