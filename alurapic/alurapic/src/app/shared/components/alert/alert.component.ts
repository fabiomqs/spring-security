import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Alert, AlertType } from 'src/app/model/alert';
import { SubSink } from 'subsink';
import { AlertService } from './service/alert.service';

@Component({
    selector: 'app-alert',
    templateUrl: './alert.component.html',
    styleUrls: ['./alert.component.css']
})
export class AlertComponent implements OnInit, OnDestroy {

    private subs = new SubSink();
    @Input() timeout = 3000;
    alerts: Alert[] = [];

    constructor(private alertService: AlertService) { }
    
    ngOnInit(): void {
        this.subs.add(
            this.alertService
                .getAlert()
                .subscribe(
                    alert => {
                        if(!alert) {
                            this.alerts = [];
                            return;
                        }
                        this.alerts.push(alert);
                        setTimeout(() => this.removeAlert(alert), this.timeout);
                    }
                )
        )
    }

    removeAlert(alertToRemove: Alert) {
        this.alerts = this.alerts
            .filter(alert => alert != alertToRemove)
    }

    getAlertClass(alert: Alert) {
        if(!alert) return '';
        switch(alert.alertType) {
            case AlertType.SUCCESS:
                return 'alert alert-success';
            case AlertType.INFO:
                return 'alert alert-info';
            case AlertType.WARNING:
                return 'alert alert-warning';
            case AlertType.DANGER:
                return 'alert alert-danger';
        }
    }
    
    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }
}
