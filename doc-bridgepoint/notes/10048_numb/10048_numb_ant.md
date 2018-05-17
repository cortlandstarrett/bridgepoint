---

This work is licensed under the Creative Commons CC0 License

---

# Removal of Class Number  
### xtUML Project Analysis Note

### 1. Abstract

Quoted from issue 10048 [2.1]:  

> Class number is an unnecessary function in BP and should be removed:
> Cause - it is an obstacle for parallel development because all new object
> becomes/gives conflicts.  
> This comes from that in metamodel, class numbers are not unique, but it is
> for the tool. This can give complications for diff/merge. The same can be
> said for instance association numbers.

### 2. Document References

<a id="2.1"></a>2.1 [10048](https://support.onefact.net/issues/10048) Removal of Class Number  

### 3. Background

The `Numb` attribute on `Model Class (O_OBJ)` has been part of the metamodel
since the early days of the Shlaer-Mellor Method.  The object number (Model
Class number) was established for ordering purposes.  It was in place to
enable the analyst to order the output of printed documentation of entity
relationship diagrams (UML class diagrams).  It has existed in every version
of BridgePoint since the tool was introduced.

This attribute on the `Model Class (O_OBJ)` class in the metamodel has been
more trouble than it is worth.  

![`Model Class (O_OBJ)`](o_obj.png)  

### 4. Requirements

4.1 `Model Class.Numb` shall be deleted from the metamodel.  
4.2 `Package.Num_Rng` shall be deleted from the metamodel.  
4.3 Existing models shall be upgraded when next persisted.  
4.3.1 Existing models shall not be upgraded unless changes are made.  
4.3.2 `Load and Persist` will upgrade a model in place removing the
`Model Class.Numb` and `Package.Num_Rng` attribute values from the
persisted files.  
4.4 Users shall be prevented from editing class numbers and number ranges.  
4.4.1 A class number will not be visible on the canvas of a class diagram.  
4.4.2 A class number will not be visible in the Properties View.  
4.4.3 A class number range will not be visible on the canvas of a package.  
4.4.4 A class number range will not be visible in the Properties View.  
4.5 In places where the editor and/or model compilers sorted classes by
`Numb`, they will be changed to sort by `Name`.  
4.6 MC-3020 shall not refer to `Model Class.Numb`.  
4.7 User documentation that refers to class numbers and class number ranges
shall be updated.  
4.8 The BridgePoint unit test suite and test models shall be upgraded
with the metamodel changes, and expected results updated to match the new
schema.  

### 5. Analysis

This is a "surgical" and straight-forward issue.  However, it touches
_everything_ (editor, Verifier, MCs, documentation, tests, models).

An option could be to keep these model elements in place but disable
the editing, integrity checking and sorting based on them.  

### 6. Work Required

BridgePoint Proper
6.1 Remove `Model Class.Numb` from `ooaofooa` and `mcooa`.  
6.2 Remove `Package.Num_Rng` from `ooaofooa` and `mcooa`.  
6.3 Add upgrade in the model loader to omit class number artifacts.  
6.4 UI Access to Class Number and Number Range  
Stop drawing the class number on the canvas.  Remove access to
`Model Class.Numb` and `Package.Num_Rng` from the Properties view.  
6.4.1 Remove painting routines that supply the class number to the canvas.  
6.4.2 Remove PEI data that configures the class number in Properties.  
6.4.3 Remove painting routines that supply the number range to the canvas.  
6.4.4 Remove PEI data that configures the number range in Properties.  
6.5 Search and destroy for class sorting routines keyed with `Numb`.
Change sort routines to use `Model Class.Name`.  
6.6 Model Compiler  
6.6.1 Change `q.sys.populate.arc` to sort using Name rather than Numb.  
6.6.2 Rebuild `escher (mcmc)` and tools depending upon mcooa (`docgen`,
`integrity`, `m2x`, `x2m`).  
6.7 Search and Destroy and Update User Documentation.  
6.8 Run unit tests and fix fall-out.  

### 7. Acceptance Test

7.1 Unit tests pass.  

### End
