import { Component, OnInit } from '@angular/core';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { RestroomsListService } from './restrooms-list.service';
import { RestRoom } from '../code/restRoom';
import { MatDialog } from '@angular/material';
import { CreateRestroomComponent } from './create-restroom/create-restroom.component';
import { interval, timer } from 'rxjs';

@Component({
  selector: 'app-restrooms-list',
  templateUrl: './restrooms-list.component.html',
  styleUrls: ['./restrooms-list.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0', display: 'none' })),
      state('expanded', style({ height: '*' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class RestroomsListComponent implements OnInit {
  columnsToDisplay = ['uid', 'cityAddress', 'company', 'sensorData'];
  columnsName = ['UID', 'ADDRESS', 'COMPANY', 'STATUS'];
  restRooms: RestRoom[];
  panelOpenState = false;
  expandedElement: RestRoom;

  constructor(private restRoomsService: RestroomsListService, public dialog: MatDialog) { }

  ngOnInit() {
    timer(0, 5000).subscribe(() => {
      this.refreshRestrooms();
    });    
  }

  addRestroom() {
    const dialogRef = this.dialog.open(CreateRestroomComponent, {
      width: '350px'
    });

    dialogRef.afterClosed().subscribe((added: boolean) => {
      if(added)
        this.refreshRestrooms();
    });
  }

  refreshRestrooms() {
    this.restRoomsService.getRestrooms().subscribe(restRooms => {
      this.restRooms = restRooms;
      if(this.expandedElement != null)
          this.expandedElement = this.restRooms.find(element => element.uid == this.expandedElement.uid)
    });
  }
}