package ru.stqa.pft.addressbook.model;

import com.google.common.collect.ForwardingSet;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Groups extends ForwardingSet<GroupData> {
  private final Set<GroupData> delegate;

  public Groups() {
    this.delegate = new HashSet<>();
  }
  public Groups(Groups groups) {
    this.delegate = new HashSet<GroupData>(groups.delegate);
  }

  @Override
  protected Set<GroupData> delegate() {
    return delegate;
  }

  public Groups withAdded(GroupData group) {
    Groups groups = new Groups(this);
    groups.add(group);
    return groups;
  }

  public Groups without(GroupData group) {
    Groups groups = new Groups(this);
    groups.remove(group);
    return groups;
  }

  public Groups withModified(GroupData group) {
    Groups groups = new Groups(this);
    Optional<GroupData> matchedGroup = this.stream().filter(x -> x.getId() == group.getId()).findFirst();
    groups.remove(matchedGroup.get());
    groups.add(group);
    return groups;
  }
  
}
