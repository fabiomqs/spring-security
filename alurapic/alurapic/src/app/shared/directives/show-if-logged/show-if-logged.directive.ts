import { Directive, ElementRef, OnDestroy, OnInit, Renderer2 } from '@angular/core';
import { UserService } from 'src/app/core/user/user.service';
import { SubSink } from 'subsink';

@Directive({
  selector: '[showIfLogged]'
})
export class ShowIfLoggedDirective implements OnInit, OnDestroy {

    private subs = new SubSink();

    currentDisplay: string;

    constructor(
        private element: ElementRef<any>,
        private renderer: Renderer2,
        private userService: UserService
    ) { }
    
    
    ngOnInit(): void {
        this.currentDisplay = getComputedStyle(this.element.nativeElement).display;

        this.subs.add(
            this.userService.getUser().subscribe(
                user => {
                    if(user) {
                        this.renderer.setStyle(this.element.nativeElement, 
                            'display', this.currentDisplay);
                    } else {
                        this.currentDisplay = getComputedStyle(this.element.nativeElement).display;
                        this.renderer.setStyle(this.element.nativeElement, 'display', 'none');            
                    }
                }
            )
        )
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

}
