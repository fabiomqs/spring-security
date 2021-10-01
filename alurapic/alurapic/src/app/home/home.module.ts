import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SigninComponent } from './signin/signin.component';
import { ReactiveFormsModule } from '@angular/forms';
import { VmessageModule } from '../shared/components/vmessage/vmessage.module';
import { RouterModule } from '@angular/router';
import { SignupComponent } from './signup/signup.component';
import { HomeComponent } from './home.component';
import { HomeRoutingModule } from './signup/home.routing.module';

@NgModule({
    declarations: [
        SigninComponent,
        SignupComponent,
        HomeComponent
    ],
    imports: [
        CommonModule, 
        ReactiveFormsModule,
        VmessageModule,
        RouterModule,
        HomeRoutingModule
    ]
})
export class HomeModule { }
