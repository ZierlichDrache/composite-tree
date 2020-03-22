import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';


/**
 * Component for manipulating with the single tree node
 */
@Component({
  selector: 'app-tree-node',
  templateUrl: './tree-node.component.html',
  styleUrls: ['./tree-node.component.scss']
})
export class TreeNodeComponent implements OnInit {

  @Input()
  treeNodeValue: number;

  @Output()
  addNewNodeEvent: EventEmitter<any> = new EventEmitter();

  @Output()
  removeNodeEvent: EventEmitter<any> = new EventEmitter();

  @Output()
  updateNodeValueEvent: EventEmitter<number> = new EventEmitter();

  newValue: FormControl;

  optionsVisible = false;

  edit = false;

  ngOnInit(): void {
    this.newValue = new FormControl(this.treeNodeValue);
  }

  addNewNode() {
    this.addNewNodeEvent.emit();
  }

  removeThisNode() {
    this.removeNodeEvent.emit();
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
      this.updateNodeValueEvent.emit(this.treeNodeValue);
    }
    this.edit = false;
  }
}
