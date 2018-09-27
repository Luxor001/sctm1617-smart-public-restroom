import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { LayoutModule } from '@angular/cdk/layout';
import { ServiceWorkerModule } from '@angular/service-worker';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule, MatButtonModule, MatSidenavModule, MatIconModule, MatListModule, MatInputModule, MatSnackBarModule } from '@angular/material';

import { AppComponent } from './app.component';
import { AgmCoreModule } from '@agm/core';

import { environment } from '../environments/environment';
import { MapComponent } from './map/map.component';
import { AboutComponent } from './about/about.component';
import { ReportComponent } from './report/report.component';
import { AppHttpInterceptor } from './appHttpInterceptor';
import { keyGmap, BASE_URL} from '../environments/keys'
import { FormsModule } from '@angular/forms';
import { MarkerDetailComponent } from './map/marker-detail/marker-detail.component';

const routes: Routes = [
  {
    path: '',
    component: MapComponent
  },
  {
    path: 'map',
    component: MapComponent
  },
  {
    path: 'report',
    component: ReportComponent
  },
  {
    path: 'about',
    component: AboutComponent
  }
];

@NgModule({
  declarations: [
    AppComponent,
    MapComponent,
    AboutComponent,
    ReportComponent,
    MarkerDetailComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    LayoutModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    MatInputModule,
    MatSnackBarModule,
    FormsModule,
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: environment.production }),
    AgmCoreModule.forRoot({
      apiKey: ''
    }),
    RouterModule.forRoot(routes),
    HttpClientModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AppHttpInterceptor, multi: true },
    { provide: "BASE_URL", useValue: BASE_URL}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

