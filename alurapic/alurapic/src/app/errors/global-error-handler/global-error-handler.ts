import { LocationStrategy, PathLocationStrategy } from "@angular/common";
import { ErrorHandler, Injectable, Injector } from "@angular/core";
import { Router } from "@angular/router";

import * as StackTrace from "stacktrace-js";

import { environment } from "src/environments/environment";

import { LocalCacheService } from "src/app/core/user/local-cache.service";
import { ServerLog } from "src/app/model/server-log";
import { ServerLogService } from "./service/server-log.service";

@Injectable()
export class GlobalErrorHandler implements ErrorHandler{
    
    constructor(private injector: Injector) {}

    handleError(error: any): void {
        const location = this.injector.get(LocationStrategy);
        const url = location instanceof PathLocationStrategy ?
            location.path() : '';

        const localCacheService = this.injector.get(LocalCacheService);
        const username = localCacheService.isLogged() ? localCacheService.getUsername() : 'no user';
        const serverLogService = this.injector.get(ServerLogService);
        const router = this.injector.get(Router);

        const message = error.message 
            ? error.message : 
            error.toString();

        if(environment.production) router.navigate(['/error']);
        
        StackTrace
            .fromError(error)
            .then(
                stackFrames => {
                    const stackAsString = stackFrames
                        .map(sf => sf.toString())
                        .join('\n')
                    console.log(message)
                    console.log(stackAsString)
                    console.log('Sending message to log server')
                    const serverLog = new ServerLog('error', message, url, username, stackAsString);
                    serverLogService.log(serverLog)
                        .subscribe(() => {
                            console.log('Error logged on server')
                        }, 
                        err => {
                            console.log(err)
                            console.log('Fail to send error log to server')
                        }
                    )
                }
            )
    }
}