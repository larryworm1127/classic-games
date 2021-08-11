import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './pages/home.page';
import { HomeRoutingModule } from './home-routing.module';
import { RouterModule } from '@angular/router';
import { NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';


@NgModule({
  declarations: [HomeComponent],
  imports: [
    CommonModule,

    HomeRoutingModule,
    RouterModule,
    NgbCollapseModule
  ]
})
export class HomeModule { }
