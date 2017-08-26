package seg.jUCMNav.editors.actionContributors;

import grl.kpimodel.QualitativeMappings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.SelectAllAction;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.actions.StackAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.services.IDisposable;

import seg.jUCMNav.JUCMNavPlugin;
import seg.jUCMNav.Messages;
import seg.jUCMNav.actions.AddAndForkAction;
import seg.jUCMNav.actions.AddAndJoinAction;
import seg.jUCMNav.actions.AddAnythingAction;
import seg.jUCMNav.actions.AddBeliefAction;
import seg.jUCMNav.actions.AddBranchAction;
import seg.jUCMNav.actions.AddBranchOnStubAction;
import seg.jUCMNav.actions.AddConditionLabelAction;
import seg.jUCMNav.actions.AddContainerRefAction;
import seg.jUCMNav.actions.AddDirectionArrow;
import seg.jUCMNav.actions.AddEmptyPoint;
import seg.jUCMNav.actions.AddFailurePointAction;
import seg.jUCMNav.actions.AddGrlGraphAction;
import seg.jUCMNav.actions.AddLabelAction;
import seg.jUCMNav.actions.AddMapAction;
import seg.jUCMNav.actions.AddOrForkAction;
import seg.jUCMNav.actions.AddOrJoinAction;
import seg.jUCMNav.actions.AddResponsibilityAction;
import seg.jUCMNav.actions.AddStartPointAction;
import seg.jUCMNav.actions.AddStereotypeDefinitionsAction;
import seg.jUCMNav.actions.AddStubAction;
import seg.jUCMNav.actions.AddTimeoutPathAction;
import seg.jUCMNav.actions.AddWaitingPlaceAction;
import seg.jUCMNav.actions.BindChildren;
import seg.jUCMNav.actions.BindWithParent;
import seg.jUCMNav.actions.ChangeColorAction;
import seg.jUCMNav.actions.ChangeComponentTypeAction;
import seg.jUCMNav.actions.ChangeCorrelationAction;
import seg.jUCMNav.actions.ChangeDecompositionTypeAction;
import seg.jUCMNav.actions.ChangeImpactAnalysisAction;
import seg.jUCMNav.actions.ChangeImpactAnalysisModificationAction;
import seg.jUCMNav.actions.ChangeLinkAction;
import seg.jUCMNav.actions.ChangeLinkMandatoryAction;
import seg.jUCMNav.actions.ChangeLinkORAction;
import seg.jUCMNav.actions.ChangeLinkOptionalAction;
import seg.jUCMNav.actions.ChangeLinkXORAction;
import seg.jUCMNav.actions.ChangeStubTypeAction;
import seg.jUCMNav.actions.ChangeWaitPlaceTypeAction;
import seg.jUCMNav.actions.ConnectAction;
import seg.jUCMNav.actions.CutPathAction;
import seg.jUCMNav.actions.DeleteUnreferencedDefinitionAction;
import seg.jUCMNav.actions.DisconnectAction;
import seg.jUCMNav.actions.DisconnectTimeoutPathAction;
import seg.jUCMNav.actions.DuplicateMapAction;
import seg.jUCMNav.actions.EditStubPluginsAction;
import seg.jUCMNav.actions.EditURNLinksAction;
import seg.jUCMNav.actions.ExportAction;
import seg.jUCMNav.actions.GenerateReportAction;
import seg.jUCMNav.actions.ImportAction;
import seg.jUCMNav.actions.ListDefinitionReferencesAction;
import seg.jUCMNav.actions.MergeStartEndAction;
import seg.jUCMNav.actions.RefactorIntoStubAction;
import seg.jUCMNav.actions.SelectDefaultPaletteToolAction;
import seg.jUCMNav.actions.SetAggregateContributionAction;
import seg.jUCMNav.actions.SetNumericalContributionAction;
import seg.jUCMNav.actions.SetNumericalEvaluationAction;
import seg.jUCMNav.actions.SetNumericalImportanceAction;
import seg.jUCMNav.actions.SetQualitativeContributionAction;
import seg.jUCMNav.actions.SetQualitativeEvaluationAction;
import seg.jUCMNav.actions.SetQualitativeImportanceAction;
import seg.jUCMNav.actions.ShowContainingActorAction;
import seg.jUCMNav.actions.ShowContainingElementAction;
import seg.jUCMNav.actions.ShowEvaluationIntentionalElementV1Action;
import seg.jUCMNav.actions.ShowLinkedElementAction;
import seg.jUCMNav.actions.ShowLinkedElementAlternativeAction;
//import seg.jUCMNav.actions.ShowEvaluationIntentionalElementAction;
import seg.jUCMNav.actions.ShowLinkedElementAlternativeSubNodesAction;
import seg.jUCMNav.actions.ShowLinkedElementCompleteAction;
import seg.jUCMNav.actions.ShowLinkedElementCompleteSubNodesAction;
import seg.jUCMNav.actions.ShowLinkedElementInNewDiagramAction;
import seg.jUCMNav.actions.ShowLinkedElementLevelThreeAction;
import seg.jUCMNav.actions.ShowLinkedElementLevelTwoAction;
import seg.jUCMNav.actions.ShowNonLeafElementsInSeparateDiagramsAction;
import seg.jUCMNav.actions.StaticSlicingAction;
import seg.jUCMNav.actions.TagElementAction;
import seg.jUCMNav.actions.ToggleEvaluationRangeAction;
import seg.jUCMNav.actions.TransmogrifyAndForkOrJoinAction;
import seg.jUCMNav.actions.TransmogrifyOrForkOrJoinAction;
import seg.jUCMNav.actions.UnbindChildren;
import seg.jUCMNav.actions.UnbindFromParent;
import seg.jUCMNav.actions.concerns.ApplyConcernAction;
import seg.jUCMNav.actions.concerns.ManageConcernsAction;
import seg.jUCMNav.actions.cutcopypaste.CopyAction;
import seg.jUCMNav.actions.cutcopypaste.CutAction;
import seg.jUCMNav.actions.cutcopypaste.PasteAction;
import seg.jUCMNav.actions.debug.MakeWellFormedAction;
import seg.jUCMNav.actions.debug.SimplifyForksAndJoinsAction;
import seg.jUCMNav.actions.debug.TrimEmptyPointsAction;
import seg.jUCMNav.actions.dynamicContexts.AddDynamicContextAction;
import seg.jUCMNav.actions.dynamicContexts.AddDynamicContextContributionContextAction;
import seg.jUCMNav.actions.dynamicContexts.AddDynamicContextScenarioAction;
import seg.jUCMNav.actions.dynamicContexts.AddDynamicContextStrategyAction;
import seg.jUCMNav.actions.dynamicContexts.AddDynamicContextsGroupAction;
import seg.jUCMNav.actions.dynamicContexts.AddTimepointAction;
import seg.jUCMNav.actions.dynamicContexts.AddTimepointsGroupAction;
import seg.jUCMNav.actions.dynamicContexts.IncludeDynamicContextAction;
import seg.jUCMNav.actions.dynamicContexts.ManageChangeAction;
import seg.jUCMNav.actions.dynamicContexts.MoveDynamicContextAction;
import seg.jUCMNav.actions.features.AddFMDAction;
import seg.jUCMNav.actions.features.ReexposeFeatureAction;
import seg.jUCMNav.actions.features.SelectFeatureAction;
import seg.jUCMNav.actions.features.UnreexposeFeatureAction;
import seg.jUCMNav.actions.features.UnselectFeatureAction;
import seg.jUCMNav.actions.hyperlinks.AddHyperlinkAction;
import seg.jUCMNav.actions.hyperlinks.ChangeHyperlinkAction;
import seg.jUCMNav.actions.hyperlinks.DeleteHyperlinkAction;
import seg.jUCMNav.actions.hyperlinks.NavigateHyperlinkAction;
import seg.jUCMNav.actions.kpi.AddIndicatorGroupAction;
import seg.jUCMNav.actions.kpi.EditIndicatorGroupsAction;
import seg.jUCMNav.actions.kpi.RunAllGRLEvaluationAction;
import seg.jUCMNav.actions.metadata.EditMetadataAction;
import seg.jUCMNav.actions.palette.SelectPaletteEntryAction;
import seg.jUCMNav.actions.performance.ManageDemandAction;
import seg.jUCMNav.actions.performance.ManageResourcesAction;
import seg.jUCMNav.actions.scenarios.AddContributionContextAction;
import seg.jUCMNav.actions.scenarios.AddContributionContextGroupAction;
import seg.jUCMNav.actions.scenarios.AddEvaluationStrategyAction;
import seg.jUCMNav.actions.scenarios.AddKPIConversionAction;
import seg.jUCMNav.actions.scenarios.AddPrePostConditionAction;
import seg.jUCMNav.actions.scenarios.AddQualitativeMappingAction;
import seg.jUCMNav.actions.scenarios.AddScenarioAction;
import seg.jUCMNav.actions.scenarios.AddScenarioGroupAction;
import seg.jUCMNav.actions.scenarios.AddStartEndPointAction;
import seg.jUCMNav.actions.scenarios.AddStrategiesGroupAction;
import seg.jUCMNav.actions.scenarios.AddVariableAction;
import seg.jUCMNav.actions.scenarios.DeleteContributionRangeAction;
import seg.jUCMNav.actions.scenarios.DeleteEvaluationAction;
import seg.jUCMNav.actions.scenarios.DeleteEvaluationRangeAction;
import seg.jUCMNav.actions.scenarios.DuplicateAction;
import seg.jUCMNav.actions.scenarios.EditCodeAction;
import seg.jUCMNav.actions.scenarios.EditContributionRangeAction;
import seg.jUCMNav.actions.scenarios.EditEvaluationRangeAction;
import seg.jUCMNav.actions.scenarios.EditSecondaryCodeAction;
import seg.jUCMNav.actions.scenarios.IncludeContributionContextAction;
import seg.jUCMNav.actions.scenarios.IncludeScenarioAction;
import seg.jUCMNav.actions.scenarios.IncludeStrategyAction;
import seg.jUCMNav.actions.scenarios.MoveContributionContextAction;
import seg.jUCMNav.actions.scenarios.MoveScenarioAction;
import seg.jUCMNav.actions.scenarios.MoveStrategyAction;
import seg.jUCMNav.actions.scenarios.RunAllScenariosAction;
import seg.jUCMNav.actions.scenarios.SetComparisonStrategyAction;
import seg.jUCMNav.actions.scenarios.StartStrategyDifferenceModeAction;
import seg.jUCMNav.actions.scenarios.StopStrategyDifferenceModeAction;
import seg.jUCMNav.actions.scenarios.VariableInitializationsAction;
import seg.jUCMNav.editors.UCMNavMultiPageEditor;
import seg.jUCMNav.editors.UrnEditor;
import seg.jUCMNav.scenarios.ScenarioUtils;

/**
 * Adds actions to the action registry. Originally included in the UCMNavMultiPageEditor, this code was factored out.
 * 
 * @author jkealey, gunterm, pchen
 * 
 */
public class ActionRegistryManager implements IDisposable {

    /** the action registry that is managed */
    private ActionRegistry actionRegistry;

    /** the list of action ids that are editor actions */
    private List editorActionIDs = new ArrayList();

    /** the list of action ids that are to EditPart actions */
    private List editPartActionIDs = new ArrayList();

    /** the list of action ids that are to CommandStack actions */
    private List stackActionIDs = new ArrayList();

    /**
     * 
     * @param reg
     *            the ActionRegistry to be build
     */
    public ActionRegistryManager(ActionRegistry reg) {
        this.actionRegistry = reg;
    }

    /**
     * Adds a general action to the action registry.
     * 
     * @param action
     */
    public void addAction(IAction action) {
        getActionRegistry().registerAction(action);
    }

    /**
     * Adds an editor action to this editor.
     * 
     * <p>
     * Editor actions are actions that depend and work on the editor.
     * 
     * @param action
     *            the editor action
     */
    public void addEditorAction(EditorPartAction action) {
        getActionRegistry().registerAction(action);
        editorActionIDs.add(action.getId());
    }

    /**
     * Adds an <code>EditPart</code> action to this editor.
     * 
     * <p>
     * <code>EditPart</code> actions are actions that depend and work on the selected <code>EditPart</code>s.
     * 
     * @param action
     *            the <code>EditPart</code> action
     */
    public void addEditPartAction(SelectionAction action) {
        getActionRegistry().registerAction(action);
        editPartActionIDs.add(action.getId());
    }

    /**
     * Adds a <code>CommandStack</code> action to this editor.
     * 
     * <p>
     * <code>CommandStack</code> actions are actions that depend and work on the <code>CommandStack</code>.
     * 
     * @param action
     *            the <code>CommandStack</code> action
     */
    public void addStackAction(StackAction action) {
        getActionRegistry().registerAction(action);
        stackActionIDs.add(action.getId());
    }

    /**
     * Initializes the action registry to what is available to jUCMNav.
     * 
     * @param editor
     * @param keyBindingService
     * @param zm
     */
    public void createActions(IEditorPart editor, IKeyBindingService keyBindingService, ZoomManager zm) {
        addStackAction(new UndoAction(editor));
        addStackAction(new RedoAction(editor));

        IAction zoomIn = new ZoomInAction(zm);
        IAction zoomOut = new ZoomOutAction(zm);
        IAction selectDefaultTool = new SelectDefaultPaletteToolAction(editor);
        addAction(zoomIn);
        addAction(zoomOut);
        addAction(selectDefaultTool);
        keyBindingService.registerAction(zoomIn);
        keyBindingService.registerAction(zoomOut);
        keyBindingService.registerAction(selectDefaultTool);

        IAction action;

        action = new CopyAction(editor);
        addEditPartAction((SelectionAction) action);
        action.setAccelerator(SWT.CTRL | 'C');
        keyBindingService.registerAction(action);

        action = new CutAction(editor);
        addEditPartAction((SelectionAction) action);
        action.setAccelerator(SWT.CTRL | 'X');
        keyBindingService.registerAction(action);

        action = new PasteAction((UCMNavMultiPageEditor) editor);
        addEditPartAction((SelectionAction) action);
        action.setAccelerator(SWT.CTRL | 'V');
        keyBindingService.registerAction(action);

        action = new SelectAllAction(editor);
        addAction(action);
        action.setAccelerator(SWT.CTRL | 'A');
        keyBindingService.registerAction(action);

        for (int letter = (int) 'a'; letter <= (int) 'z'; letter++) {
            if (UrnEditor.keybindingExcludes.indexOf((char) letter) != -1) // reserve some keys for other uses
                continue;

            action = new SelectPaletteEntryAction(editor, (char) letter);
            addAction(action);
        }
        
        for (int letter = (int) '0'; letter <= (int) '9'; letter++) {
            if (UrnEditor.keybindingExcludes.indexOf((char) letter) != -1) // reserve some keys for other uses
                continue;

            action = new SelectPaletteEntryAction(editor, (char) letter);
            addAction(action);
        }

        char[] extraChars = new char[] { '>', '-', ' ' };
        for (int i = 0; i < extraChars.length; i++) {
            char c = extraChars[i];
            if (UrnEditor.keybindingExcludes.indexOf(c) != -1) // reserve some keys for other uses
                continue;
            action = new SelectPaletteEntryAction(editor, c);
            addAction(action);
        }

        // Notice the following are calls to addEditPartAction().
        // They need to know the current selection to work.
        // If you write addAction instead, you'll get empty selections
        
        action = new seg.jUCMNav.actions.DeleteAction(editor);
        addEditPartAction((SelectionAction) action);

        action = new DeleteEvaluationAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.deleteUserEvaluation")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new DeleteEvaluationRangeAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.DeleteEvaluationRange")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        //GRL Change Impact Analysis Addition/Deletion action @hasanKaff
        action = new ChangeImpactAnalysisAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ChangeImpactAnalysis"));
        addEditPartAction((SelectionAction) action);
        
        //GRL Change Impact Analysis Modification action @hasanKaff
        action = new ChangeImpactAnalysisModificationAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ChangeImpactAnalysisModification"));
        addEditPartAction((SelectionAction) action);
        
        action = new DeleteContributionRangeAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.DeleteContributionRange")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new CutPathAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.cutPath")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action=new StaticSlicingAction(editor);
        action.setText("Static Slicing");
        addEditPartAction((SelectionAction)action);
        
        action = new RefactorIntoStubAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.RefactorIntoStub")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddLabelAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addLabel")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddDirectionArrow(editor);
        action.setText(Messages.getString("ActionRegistryManager.addDirectionArrow")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddEmptyPoint(editor);
        action.setText(Messages.getString("ActionRegistryManager.addEmptyPoint")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddResponsibilityAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addResponsibility")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        for (int i = 0; i < 10; i++) {
            action = new ChangeStubTypeAction(editor, i);
            // set text done in action.
            addEditPartAction((SelectionAction) action);
        }

        for (int i = 0; i < 2; i++) {
            action = new ChangeWaitPlaceTypeAction(editor, i);
            // set text done in action.
            addEditPartAction((SelectionAction) action);
        }

        for (int i = 0; i < 6; i++) {
            action = new ChangeComponentTypeAction(editor, i);
            // set text done in action.
            addEditPartAction((SelectionAction) action);
        }

        for (int i = 0; i < 10; i++) {
            action = new AddStubAction(editor, i);
            addEditPartAction((SelectionAction) action);
        }

        action = new AddWaitingPlaceAction(editor, AddWaitingPlaceAction.ADDWP);
        action.setText(Messages.getString("ActionRegistryManager.addWaitingPlace")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddWaitingPlaceAction(editor, AddWaitingPlaceAction.ADDTIMER);
        action.setText(Messages.getString("ActionRegistryManager.addTimer")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new AddAnythingAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addAnything")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddFailurePointAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addFailurePoint")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddConditionLabelAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.AddConditionLabel")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new MergeStartEndAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.mergeStartEnd")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddOrForkAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addOrFork")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddAndForkAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addAndFork")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new TransmogrifyOrForkOrJoinAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.convertToAnd")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new TransmogrifyAndForkOrJoinAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.convertToOr")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddOrJoinAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addOrJoin")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddAndJoinAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addAndJoin")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddStartPointAction((UCMNavMultiPageEditor) editor);
        action.setText(Messages.getString("ActionRegistryManager.AddStartPoint")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddContainerRefAction((UCMNavMultiPageEditor) editor, AddContainerRefAction.ADDTEAM);
        action.setText(Messages.getString("ActionRegistryManager.AddTeam")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddContainerRefAction((UCMNavMultiPageEditor) editor, AddContainerRefAction.ADDOBJECT);
        action.setText(Messages.getString("ActionRegistryManager.AddObject")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddContainerRefAction((UCMNavMultiPageEditor) editor, AddContainerRefAction.ADDPROCESS);
        action.setText(Messages.getString("ActionRegistryManager.AddProcess")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddContainerRefAction((UCMNavMultiPageEditor) editor, AddContainerRefAction.ADDAGENT);
        action.setText(Messages.getString("ActionRegistryManager.AddAgent")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddContainerRefAction((UCMNavMultiPageEditor) editor, AddContainerRefAction.ADDACTOR);
        action.setText(Messages.getString("ActionRegistryManager.AddActor")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddContainerRefAction((UCMNavMultiPageEditor) editor, AddContainerRefAction.ADDOTHER);
        action.setText(Messages.getString("ActionRegistryManager.AddOther")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddBranchAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addBranch")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddBranchOnStubAction(editor, true);
        action.setText(Messages.getString("ActionRegistryManager.AddIncomingBranch")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddBranchOnStubAction(editor, false);
        action.setText(Messages.getString("ActionRegistryManager.AddOutgoingBranch")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddTimeoutPathAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addTimeoutPath")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new DisconnectTimeoutPathAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.disconnectTimeoutPath")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new ConnectAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.connectElements")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new DeleteUnreferencedDefinitionAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.DeleteUnreferencedDefinitions")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new ToggleEvaluationRangeAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ToggleEvaluationRange")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new DisconnectAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.disconnectElements")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new BindWithParent(editor);
        action.setText(Messages.getString("ActionRegistryManager.bindWithParent")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new UnbindFromParent(editor);
        action.setText(Messages.getString("ActionRegistryManager.unbindFromParent")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new UnbindChildren(editor);
        action.setText(Messages.getString("ActionRegistryManager.unbindAll")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new BindChildren(editor);
        action.setText(Messages.getString("ActionRegistryManager.bindAll")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new SelectFeatureAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.SelectFeature")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new UnselectFeatureAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.UnselectFeature")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ReexposeFeatureAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ReexposeFeature")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new UnreexposeFeatureAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.UnreexposeFeature")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new AddBeliefAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addBelief")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new ShowNonLeafElementsInSeparateDiagramsAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ShowNonLeafElementsInSeparateDiagrams")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        for (int i = 0; i <= 2; i++) {
            action = new ChangeDecompositionTypeAction(editor, i);
            addEditPartAction((SelectionAction) action);
        }
        
        for (int i = 0; i <= 2; i++) {
            action = new SetAggregateContributionAction(editor, i);
            addEditPartAction((SelectionAction) action);
        }

        for (int i = 0; i <= 7; i++) {
            action = new SetNumericalImportanceAction(editor, i);
            addEditPartAction((SelectionAction) action);
        }

        for (int i = 0; i <= 5; i++) {
            action = new SetQualitativeImportanceAction(editor, i);
            addEditPartAction((SelectionAction) action);
        }

        for (int i = 0; i <= 11; i++) {
            action = new SetNumericalEvaluationAction(editor, i);
            addEditPartAction((SelectionAction) action);
        }

        for (int i = 0; i <= 8; i++) {
            action = new SetQualitativeEvaluationAction(editor, i);
            addEditPartAction((SelectionAction) action);
        }

        for (int i = 0; i <= 11; i++) {
            action = new SetNumericalContributionAction(editor, i);
            addEditPartAction((SelectionAction) action);
        }

        for (int i = 0; i <= 8; i++) {
            action = new SetQualitativeContributionAction(editor, i);
            addEditPartAction((SelectionAction) action);
        }

        action = new TagElementAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.tagElement")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new AddStereotypeDefinitionsAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addStereotypeDefinitions")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new ChangeCorrelationAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.changeCorrelation")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddMapAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addMap")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new DirectEditAction((IWorkbenchPart) editor);
        action.setText(Messages.getString("ActionRegistryManager.DirectEdit")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new ExportAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.exportImage")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new ImportAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.import")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        for (int i = 0; i < 16; i++) {
            action = new ChangeColorAction(editor, i);
            addEditPartAction((SelectionAction) action);
        }

        action = new GenerateReportAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.report")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new EditURNLinksAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.editURNLinks")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddGrlGraphAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addGRLGraph")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddFMDAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addFMD")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddStrategiesGroupAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addEvaluationGroup")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new AddContributionContextGroupAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.AddContributionContextGroup")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddContributionContextAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.AddContributionContext")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new AddIndicatorGroupAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addIndicatorGroup")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddEvaluationStrategyAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.addEvaluationStrategy")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddScenarioGroupAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.AddScenarioGroup")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddScenarioAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.AddScenario")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddVariableAction(editor, ScenarioUtils.sTypeBoolean);
        action.setText(Messages.getString("ActionRegistryManager.NewBoolean")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddVariableAction(editor, ScenarioUtils.sTypeInteger);
        action.setText(Messages.getString("ActionRegistryManager.NewInteger")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddVariableAction(editor, ""); //$NON-NLS-1$
        action.setText(Messages.getString("ActionRegistryManager.NewVariableWizard")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddVariableAction(editor, ScenarioUtils.sTypeEnumeration);
        action.setText(Messages.getString("ActionRegistryManager.EditEnumerations")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddKPIConversionAction(editor, QualitativeMappings.class);
        action.setText(Messages.getString("ActionRegistryManager.AddQualitativeMappingGroup")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new AddQualitativeMappingAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.AddQualitativeMapping")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new EditCodeAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.Edit")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new EditSecondaryCodeAction(editor);
        action.setText("Edit Failure Condition"); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new IncludeScenarioAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.IncludeScenario")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new IncludeStrategyAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.IncludeStrategy")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new IncludeContributionContextAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.IncludeContributionContext")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new AddStartEndPointAction(editor, true);
        action.setText(Messages.getString("ActionRegistryManager.AddStartPoint")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddStartEndPointAction(editor, false);
        action.setText(Messages.getString("ActionRegistryManager.AddEndPoint")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddPrePostConditionAction(editor, true);
        action.setText(Messages.getString("ActionRegistryManager.AddPrecondition")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddPrePostConditionAction(editor, false);
        action.setText(Messages.getString("ActionRegistryManager.AddPostcondition")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new VariableInitializationsAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.InitializeVariables")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new MoveScenarioAction(editor, true);
        action.setText(Messages.getString("ActionRegistryManager.MoveUp")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new MoveScenarioAction(editor, false);
        action.setText(Messages.getString("ActionRegistryManager.MoveDown")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new MoveStrategyAction(editor, true);
        action.setText(Messages.getString("ActionRegistryManager.MoveUp")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new MoveStrategyAction(editor, false);
        action.setText(Messages.getString("ActionRegistryManager.MoveDown")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new MoveContributionContextAction(editor, true);
        action.setText(Messages.getString("ActionRegistryManager.MoveUp")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new MoveContributionContextAction(editor, false);
        action.setText(Messages.getString("ActionRegistryManager.MoveDown")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);        
        
        action = new DuplicateAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.Duplicate")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new DuplicateMapAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.Duplicate")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new ManageConcernsAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ManageConcern")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new ApplyConcernAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ApplyConcern")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new EditMetadataAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.editMetadata")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new EditEvaluationRangeAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.EditEvaulationRange")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new EditContributionRangeAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.EditContributionRange")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new EditIndicatorGroupsAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.editIndicatorGroup")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new RunAllScenariosAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.RunAllScenariosInGroup")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new RunAllGRLEvaluationAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.RunAllGRLEvaluationAction")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new ManageDemandAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ManageDemandAction")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new ManageResourcesAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ManageResourcesAction")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddHyperlinkAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.AddHyperlinkAction")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ShowLinkedElementAlternativeAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ShowLinkedElementAlternative")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ShowLinkedElementAlternativeSubNodesAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ShowLinkedElementAlternativeSubNodes")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ChangeLinkAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ChangeLink")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ChangeLinkMandatoryAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ChangeLinkMandatory")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ChangeLinkOptionalAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ChangeLinkOptional")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ChangeLinkORAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ChangeLinkOR")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ChangeLinkXORAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ChangeLinkXOR")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ShowLinkedElementCompleteAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ShowLinkedElementComplete")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ShowLinkedElementCompleteSubNodesAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ShowLinkedElementCompleteSubNodes")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ShowLinkedElementAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ShowLinkedElement")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ShowLinkedElementLevelTwoAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ShowLinkedElementLevelTwo")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ShowLinkedElementLevelThreeAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ShowLinkedElementLevelThree")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ShowLinkedElementInNewDiagramAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ShowLinkedElementInNewDiagram")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ShowContainingElementAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ShowContainingElement")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ShowContainingActorAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ShowContainingActor")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ShowEvaluationIntentionalElementV1Action(editor);
        action.setText(Messages.getString("ActionRegistryManager.ShowEvaluationIntentionalElement")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new NavigateHyperlinkAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.NavigateHyperlinkAction")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new ChangeHyperlinkAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ChangeHyperlinkAction")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new DeleteHyperlinkAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.DeleteHyperlinkAction")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new StartStrategyDifferenceModeAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.StartStrategyDifferenceModeAction")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new StopStrategyDifferenceModeAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.StopStrategyDifferenceModeAction")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new SetComparisonStrategyAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.SetComparisonStrategyAction")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ListDefinitionReferencesAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.References")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ShowLinkedElementAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ShowLinkedElement")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        //Dynamic Context Elements
        action = new AddDynamicContextsGroupAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.AddDynamicContextGroup")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddDynamicContextAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.AddDynamicContext")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new IncludeDynamicContextAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.IncludeDynamicContext")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new AddDynamicContextStrategyAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.AddDynamicContextStrategy")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new AddDynamicContextScenarioAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.AddDynamicContextScenario")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new AddDynamicContextContributionContextAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.AddDynamicContextContributionContext")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new MoveDynamicContextAction(editor, true);
        action.setText(Messages.getString("ActionRegistryManager.MoveUp")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new MoveDynamicContextAction(editor, false);
        action.setText(Messages.getString("ActionRegistryManager.MoveDown")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new AddTimepointsGroupAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.AddTimepointGroup")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        action = new AddTimepointAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.AddTimepoint")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        action = new ManageChangeAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.ManageChange")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);
        
        // keep at bottom
        action = new EditStubPluginsAction(editor);
        action.setText(Messages.getString("ActionRegistryManager.editStubPlugins")); //$NON-NLS-1$
        addEditPartAction((SelectionAction) action);

        // only available when debugging jucmnav

        if (JUCMNavPlugin.isInDebug()) {
            action = new MakeWellFormedAction(editor);
            action.setText("Transform URN so that concurrency is linearizable (well-formed)."); //$NON-NLS-1$
            addEditPartAction((SelectionAction) action);

            action = new SimplifyForksAndJoinsAction(editor);
            action.setText("Simplify Forks and Joins."); //$NON-NLS-1$
            addEditPartAction((SelectionAction) action);

            action = new TrimEmptyPointsAction(editor);
            action.setText("Delete all empty points."); //$NON-NLS-1$
            addEditPartAction((SelectionAction) action);
        }

    }

    /**
     * @return The managed action registry.
     */
    public ActionRegistry getActionRegistry() {
        return actionRegistry;
    }

    /**
     * @return The list of all actions added by addEditorAction()
     */
    public List getEditorActionIDs() {
        return editorActionIDs;
    }

    /**
     * @return The list of all actions added by addEditorAction()
     */
    public List getEditPartActionIDs() {
        return editPartActionIDs;
    }

    /**
     * @return The list of all actions added by addEditorAction()
     */
    public List getStackActionIDs() {
        return stackActionIDs;
    }

    /**
     * Shorthand for updateActions(editorActionIDs); updateActions(editPartActionIDs); updateActions(stackActionIDs);
     * 
     */
    public void updateActions() {
        updateActions(editorActionIDs);
        updateActions(editPartActionIDs);
        updateActions(stackActionIDs);
    }

    /**
     * Updates the specified actions, if they are UpdateActions
     * 
     * @param actionIds
     *            the list of ids of actions to update
     */
    public void updateActions(List actionIds) {
        for (Iterator ids = actionIds.iterator(); ids.hasNext();) {
            IAction action = getActionRegistry().getAction(ids.next());
            if (null != action && action instanceof UpdateAction)
                ((UpdateAction) action).update();
            
        }
    }

    /**
     * Shorthand for updateActions(editorActionIDs);
     * 
     */
    public void updateEditorActions() {
        updateActions(editorActionIDs);
    }

    /**
     * Shorthand forupdateActions(editPartActionIDs);
     * 
     */
    public void updateEditPartActions() {
        updateActions(editPartActionIDs);
    }

    /**
     * Shorthand for updateActions(stackActionIDs);
     * 
     */
    public void updateStackActions() {
        updateActions(stackActionIDs);
    }

    public void dispose() {
        getActionRegistry().dispose();
        actionRegistry = null;
    }
}
