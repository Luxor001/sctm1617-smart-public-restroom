import { Component, OnInit } from '@angular/core';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { RestroomsListService } from './restrooms-list.service';
import { RestRoom } from '../code/restRoom';
import { MatDialog } from '@angular/material';
import { CreateRestroomComponent } from './create-restroom/create-restroom.component';

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
  columnsToDisplay = ['id', 'cityAddress', 'company', 'position'];
  expandedElement: PeriodicElement;
  restRooms: RestRoom[];
  panelOpenState = false;

  constructor(private restRoomsService: RestroomsListService, public dialog: MatDialog) { }

  ngOnInit() {
    this.restRoomsService.getRestrooms().subscribe(restRooms => {
      this.restRooms = restRooms;
    });
  }

  addRestroom() {
    const dialogRef = this.dialog.open(CreateRestroomComponent, {
      width: '350px'
    });
  }
}


export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
  description: string;
}