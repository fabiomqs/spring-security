import { Injectable, OnDestroy, OnInit } from '@angular/core';
import { NavigationStart, Router } from '@angular/router';
import { Subject } from 'rxjs';
import { Alert, AlertType } from 'src/app/model/alert';
import { SubSink } from 'subsink';

@Injectable({
    providedIn: 'root'
})
export class AlertService implements OnInit, OnDestroy{

    private subs = new SubSink();

    alertSubject = new Subject<Alert>();
    keepAfterRouteChange = false;

    constructor(private router: Router) {}
    
    ngOnInit(): void {
        this.subs.add(
            this.router.events
                .subscribe(event => {
                    if(event instanceof NavigationStart) {
                        if(this.keepAfterRouteChange) {
                            this.keepAfterRouteChange = false;
                        } else {
                            this.clear();
                        }
                    }
                })
        );
    }

    success(message: string, keepAfterRouteChange: boolean = false) {
        this.alert(AlertType.SUCCESS, message, keepAfterRouteChange);
    }

    info(message: string, keepAfterRouteChange: boolean = false) {
        this.alert(AlertType.INFO, message, keepAfterRouteChange);
    }

    warning(message: string, keepAfterRouteChange: boolean = false) {
        this.alert(AlertType.WARNING, message, keepAfterRouteChange);
    }

    danger(message: string, keepAfterRouteChange: boolean = false) {
        this.alert(AlertType.DANGER, message, keepAfterRouteChange);
    }

    private alert(alertType: AlertType, message: string, keepAfterRouteChange: boolean) {
        this.keepAfterRouteChange = keepAfterRouteChange;
        this.alertSubject.next(new Alert(alertType, message));
    }

    getAlert() {
        return this.alertSubject.asObservable();
    }

    private clear() {
        this.alertSubject.next(null);
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

}
