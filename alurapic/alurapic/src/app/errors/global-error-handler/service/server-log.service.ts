import { HttpClient, HttpParams } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { LOGURL } from 'src/app/core/tokens';
import { ServerLog } from 'src/app/model/server-log';

@Injectable({
    providedIn: 'root'
})
export class ServerLogService {

    constructor(
        private http:HttpClient,
        @Inject(LOGURL) private logUrl: string
    ) { }

    log(serverLog: ServerLog) {
        const params = new HttpParams()
                .append('type', serverLog.type)
                .append('message', serverLog.message)
                .append('stack', serverLog.stack)
                .append('url', serverLog.url)
                .append('username', serverLog.username)
        return this.http.post(`${this.logUrl}/log`, null, { params : params });
    }
}
