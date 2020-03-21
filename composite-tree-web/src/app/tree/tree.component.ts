import { FlatTreeControl } from '@angular/cdk/tree';
import { Component, OnInit } from '@angular/core';
import { MatTreeFlatDataSource, MatTreeFlattener } from '@angular/material/tree';
import { PartialObserver } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { ComponentService } from '../shared/component.service';
import { ComponentNode } from '../shared/dtos';
import { ComponentFlatNode } from '../shared/interfaces';
import { TreeUtils } from './tree-util';

@Component({
  selector: 'app-tree',
  templateUrl: './tree.component.html',
  styleUrls: ['./tree.component.scss']
})
export class TreeComponent implements OnInit {

  treeControl = new FlatTreeControl<ComponentFlatNode>(
    node => node.level, node => node.expandable);

  treeFlattener = new MatTreeFlattener(
    TreeUtils.transformer, node => node.level, node => node.expandable, node => node.children);

  dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);

  hasChild = TreeUtils.hasChild;

  private data: ComponentNode;

  private getRootComponentNodeObserver: PartialObserver<ComponentNode> = {
    next: value => {
      this.data = value;
      this.dataSource.data = [this.data];
      this.treeControl.expandAll();
    }
  };

  constructor(private service: ComponentService) {
  }

  ngOnInit(): void {
    this.service.getRootComponent().subscribe(this.getRootComponentNodeObserver);
  }

  saveRootComponentNode() {
    this.service.updateRootComponent(this.data).subscribe(this.getRootComponentNodeObserver);
  }

  onAddNewNodeEvent(id: number) {
    this.service.addNewLeafToComponent(id).subscribe({
      next: value => {
        this.data = this.updateData(id, value, this.data);
        this.dataSource.data = [this.data];
        this.treeControl.expandAll();
      }
    });
  }

  onRemoveNodeEvent(id: number) {
    this.service.deleteComponent(id)
      .pipe(switchMap(() => this.service.getRootComponent()))
      .subscribe(this.getRootComponentNodeObserver);
  }

  onUpdateNodeValueEvent(id: number, newValue: number) {
    this.service.updateCompnentValue(id, newValue)
      .pipe(switchMap(() => this.service.getRootComponent()))
      .subscribe(this.getRootComponentNodeObserver);
  }

  private updateData(id: number, newNode: ComponentNode, data: ComponentNode) {
    if (data.id === id) {
      data = newNode;
    } else if (!!data.children) {
      data.children = data.children.map((item) => {
        return this.updateData(id, newNode, item);
      });
    }
    return data;
  }
}
