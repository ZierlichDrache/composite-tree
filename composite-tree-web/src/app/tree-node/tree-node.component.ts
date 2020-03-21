import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';


@Component({
  selector: 'app-tree-node',
  templateUrl: './tree-node.component.html',
  styleUrls: ['./tree-node.component.scss']
})
export class TreeNodeComponent implements OnInit {

  @Input()
  treeNodeValue = 0;

  @Output()
  addNewLeafEvent: EventEmitter<any> = new EventEmitter();

  @Output()
  removeComponentEvent: EventEmitter<any> = new EventEmitter();

  @Output()
  updateValueEvent: EventEmitter<number> = new EventEmitter();

  newValue: FormControl;

  optionsVisible = false;

  edit = false;

  ngOnInit(): void {
    this.newValue = new FormControl(this.treeNodeValue);
  }

  addNewLeaf() {
    this.addNewLeafEvent.emit();
  }

  removeComponent() {
    this.removeComponentEvent.emit();
  }

  showOptions(): void {
    this.optionsVisible = true;
  }

  hideOptions(): void {
    this.edit = false;
    this.optionsVisible = false;
  }

  cancelEdit() {
    this.edit = false;
  }

  performEdit() {
    this.edit = true;
  }

  acceptEditWithNewValue() {
    if (this.treeNodeValue !== this.newValue.value) {
      this.treeNodeValue = this.newValue.value;
    }
    this.edit = false;
    this.updateValueEvent.emit(this.treeNodeValue);
  }
}
