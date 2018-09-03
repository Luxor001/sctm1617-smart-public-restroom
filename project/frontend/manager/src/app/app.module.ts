import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { LayoutModule } from '@angular/cdk/layout';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule, MatButtonModule, MatSidenavModule, MatIconModule, MatListModule, MatInputModule, MatTableModule } from '@angular/material';

import { AppComponent } from './app.component';

import { AppHttpInterceptor } from './appHttpInterceptor';
import { FormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { AuthGuard } from './guards/AuthGuard';
import { RestroomsListComponent } from './restrooms-list/restrooms-list.component';
import { LoginGuard } from './guards/LoginGuard';
import { CreateRestroomComponent } from './create-restroom/create-restroom.component';
import { BASE_URL } from '../environments/keys';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'restrooms', component: RestroomsListComponent, canActivate: [AuthGuard]},
      { path: 'create', component: CreateRestroomComponent, canActivate: [AuthGuard]}
    ]
  },
  { path: 'login', component: LoginComponent, canActivate: [LoginGuard]},
  { path: '**', redirectTo: ''} // questo handler deve SEMPRE rimanere per ultimo
];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    RestroomsListComponent,
    CreateRestroomComponent
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
    MatTableModule,
    FormsModule,
    RouterModule.forRoot(routes),
    HttpClientModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AppHttpInterceptor, multi: true },
    { provide: "BASE_URL", useValue: BASE_URL},
    AuthGuard,
    LoginGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

