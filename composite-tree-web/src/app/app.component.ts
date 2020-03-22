import { Component, OnInit } from '@angular/core';
import { LoaderService } from './loader.service';

/**
 * The main app
 */
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  isLoading = false;

  constructor(private loaderService: LoaderService){}

  ngOnInit() {
    this.loaderService.isLoading$.subscribe(value => this.isLoading = value);
  }
}
