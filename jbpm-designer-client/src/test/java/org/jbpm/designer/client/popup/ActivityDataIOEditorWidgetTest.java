/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.designer.client.popup;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.jbpm.designer.client.shared.AssignmentRow;
import org.jbpm.designer.client.shared.Variable;
import org.jbpm.designer.client.util.ListBoxValues;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class ActivityDataIOEditorWidgetTest {

    @Mock
    private AssignmentRow assignmentRowOne;

    @Mock
    private AssignmentRow assignmentRowTwo;

    @Mock
    private AssignmentRow assignmentRowThree;

    @Mock
    private AssignmentListItemWidget assignWidgetOne;

    @Mock
    private AssignmentListItemWidget assignWidgetTwo;

    @Mock
    private AssignmentListItemWidget assignWidgetThree;

    @Mock
    private ActivityDataIOEditorWidgetView view;

    @Spy
    @InjectMocks
    private ActivityDataIOEditorWidget widget = new ActivityDataIOEditorWidget();

    private List<AssignmentRow> rows;

    @Before
    public void initTestCase() {
        when(assignmentRowOne.getName()).thenReturn("aBc");
        when(assignmentRowTwo.getName()).thenReturn("aBc");
        when(assignmentRowThree.getName()).thenReturn("def");

        rows = new ArrayList<AssignmentRow>();
        rows.add(assignmentRowOne);
        rows.add(assignmentRowTwo);
        rows.add(assignmentRowThree);

        when(view.getAssignmentRows()).thenReturn(rows);
        when(view.getAssignmentsCount()).thenReturn(rows.size());

        when(view.getAssignmentWidget(0)).thenReturn(assignWidgetOne);
        when(view.getAssignmentWidget(1)).thenReturn(assignWidgetTwo);
        when(view.getAssignmentWidget(2)).thenReturn(assignWidgetThree);
    }

    @Test
    public void testInit() {
        widget.init();

        verify(view).init(widget);
    }

    @Test
    public void testAddHandlerSingleFull() {
        widget.setIsSingleVar(true);
        widget.handleAddClick();

        verify(view).showOnlySingleEntryAllowed();
        verify(view, never()).getAssignmentWidget(anyInt());
    }

    @Test
    public void testAddHandlerSingle() {
        rows.clear();
        when(view.getAssignmentRows()).thenReturn(rows);
        when(view.getAssignmentsCount()).thenReturn(1);

        widget.setIsSingleVar(true);
        widget.handleAddClick();

        verify(view, never()).showOnlySingleEntryAllowed();
        verify(view).getAssignmentWidget(0);
        verify(view, never()).getAssignmentWidget(1);
        verify(view, never()).getAssignmentWidget(2);
    }

    @Test
    public void testAddHandlerMultiple() {
        rows.remove(assignmentRowThree);
        when(view.getAssignmentRows()).thenReturn(rows);
        when(view.getAssignmentsCount()).thenReturn(3);

        widget.setIsSingleVar(false);
        widget.handleAddClick();

        verify(view, never()).showOnlySingleEntryAllowed();
        verify(view, never()).getAssignmentWidget(0);
        verify(view, never()).getAssignmentWidget(1);
        verify(view).getAssignmentWidget(2);
    }

    @Test
    public void testSingleVarInput() {
        widget.setVariableType(Variable.VariableType.INPUT);

        widget.setIsSingleVar(true);

        verify(view).setProcessVarAsSource();
        verify(view).setTableTitleInputSingle();
    }

    @Test
    public void testMultipleVarInput() {
        widget.setVariableType(Variable.VariableType.INPUT);

        widget.setIsSingleVar(false);

        verify(view).setProcessVarAsSource();
        verify(view).setTableTitleInputMultiple();
    }

    @Test
    public void testSingleVarOutput() {
        widget.setVariableType(Variable.VariableType.OUTPUT);

        widget.setIsSingleVar(true);

        verify(view).setProcessVarAsTarget();
        verify(view).setTableTitleOutputSingle();
    }

    @Test
    public void testMultipleVarOutput() {
        widget.setVariableType(Variable.VariableType.OUTPUT);

        widget.setIsSingleVar(false);

        verify(view).setProcessVarAsTarget();
        verify(view).setTableTitleOutputMultiple();
    }

    @Test
    public void testRemoveAssignmentEmpty() {
        widget.removeAssignment(assignmentRowOne);
        widget.removeAssignment(assignmentRowTwo);
        widget.removeAssignment(assignmentRowThree);

        verify(view).setNoneDisplayStyle();
        assertEquals(0, view.getAssignmentRows().size());
    }

    @Test
    public void testRemoveAssignmentNonEmpty() {
        widget.removeAssignment(assignmentRowOne);
        widget.removeAssignment(assignmentRowThree);

        verify(view, never()).setNoneDisplayStyle();
        assertEquals(1, view.getAssignmentRows().size());
        assertEquals(assignmentRowTwo, view.getAssignmentRows().get(0));
    }

    @Test
    public void testSetDataEmpty() {
        rows.clear();
        widget.setData(rows);

        verify(view).setNoneDisplayStyle();
        verify(view).setAssignmentRows(rows);

        verify(view, never()).getAssignmentWidget(anyInt());
    }

    @Test
    public void testSetDataNonEmpty() {
        widget.setData(rows);

        verify(view).setTableDisplayStyle();
        verify(view).setAssignmentRows(rows);

        verify(view, times(3)).getAssignmentWidget(0);
        verify(view, times(3)).getAssignmentWidget(1);
        verify(view, times(3)).getAssignmentWidget(2);

        verify(assignWidgetOne).setParentWidget(widget);
        verify(assignWidgetOne).setDisallowedNames(new HashSet<String>(), "");
        verify(assignWidgetOne).setAllowDuplicateNames(true, "");

        verify(assignWidgetTwo).setParentWidget(widget);
        verify(assignWidgetTwo).setDisallowedNames(new HashSet<String>(), "");
        verify(assignWidgetTwo).setAllowDuplicateNames(true, "");

        verify(assignWidgetThree).setParentWidget(widget);
        verify(assignWidgetThree).setDisallowedNames(new HashSet<String>(), "");
        verify(assignWidgetThree).setAllowDuplicateNames(true, "");
    }

    @Test
    public void testSetDataComplex() {
        Set<String> disallowed = new HashSet<String>();
        disallowed.add("abc");

        widget.setDisallowedNames(disallowed, "message1");
        widget.setAllowDuplicateNames(false, "message2");

        widget.setData(rows);

        verify(view).setTableDisplayStyle();
        verify(view).setAssignmentRows(rows);

        verify(view, times(4)).getAssignmentWidget(0); //setData:3 + setDisallowed:1
        verify(view).getAssignmentWidget(1);
        verify(view).getAssignmentWidget(2);

        //there should be assignWidgetThree but is not because of test initialization in @Before
        verify(assignWidgetOne).setParentWidget(widget);
        verify(assignWidgetOne, times(2)).setDisallowedNames(disallowed, "message1");
        verify(assignWidgetOne).setAllowDuplicateNames(false, "message2");
    }

    @Test
    public void testGetData() {
        List<AssignmentRow> rows = widget.getData();

        assertEquals(3, rows.size());
        assertEquals(assignmentRowOne, rows.get(0));
        assertEquals(assignmentRowTwo, rows.get(1));
        assertEquals(assignmentRowThree, rows.get(2));
    }

    @Test
    public void testGetDataWithHiddenNotMatch() {
        Set<String> disallowed = new HashSet<String>();
        disallowed.add("x");

        widget.setDisallowedNames(disallowed, "message");
        widget.setData(rows);

        List<AssignmentRow> result = widget.getData();
        assertEquals(rows, result);
    }

    @Test
    public void testGetDataWithHiddenMatch() {
        Set<String> disallowed = new HashSet<String>();
        disallowed.add("abc");

        widget.setDisallowedNames(disallowed, "message");
        widget.setData(rows);

        List<AssignmentRow> result = widget.getData();
        assertEquals(3, result.size());
        assertTrue(result.contains(assignmentRowOne));
        assertTrue(result.contains(assignmentRowTwo));
        assertTrue(result.contains(assignmentRowThree));
        assertNotEquals(rows, result);
    }

    @Test
    public void testDataTypes() {
        ListBoxValues types = mock(ListBoxValues.class);
        widget.setDataTypes(types);

        verify(view).getAssignmentWidget(0);
        verify(view).getAssignmentWidget(1);
        verify(view).getAssignmentWidget(2);

        verify(assignWidgetOne).setDataTypes(types);
        verify(assignWidgetTwo).setDataTypes(types);
        verify(assignWidgetThree).setDataTypes(types);
    }

    @Test
    public void testProcessVariables() {
        ListBoxValues variables = mock(ListBoxValues.class);
        widget.setProcessVariables(variables);

        verify(view).getAssignmentWidget(0);
        verify(view).getAssignmentWidget(1);
        verify(view).getAssignmentWidget(2);

        verify(assignWidgetOne).setProcessVariables(variables);
        verify(assignWidgetTwo).setProcessVariables(variables);
        verify(assignWidgetThree).setProcessVariables(variables);
    }

    @Test
    public void testSetDisallowedNames() {
        Set<String> names = mock(new HashSet<String>().getClass());
        widget.setDisallowedNames(names, "message");

        verify(view).getAssignmentWidget(0);
        verify(view).getAssignmentWidget(1);
        verify(view).getAssignmentWidget(2);

        verify(assignWidgetOne).setDisallowedNames(names, "message");
        verify(assignWidgetTwo).setDisallowedNames(names, "message");
        verify(assignWidgetThree).setDisallowedNames(names, "message");
    }

    @Test
    public void testIsDuplicateName() {
        assertTrue(widget.isDuplicateName("aBc"));
        assertFalse(widget.isDuplicateName("def"));
        assertFalse(widget.isDuplicateName("q"));
    }

    @Test
    public void testSetIsVisible() {
        widget.setIsVisible(true);
        verify( view ).setVisible(true);

        widget.setIsVisible(false);
        verify( view ).setVisible(false);
    }
}
