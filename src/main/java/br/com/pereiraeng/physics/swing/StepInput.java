package br.com.pereiraeng.physics.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import br.com.pereiraeng.physics.Grandeza;
import br.com.pereiraeng.swing.Grade;
import br.com.pereiraeng.swing.input.DoubleInput;
import br.com.pereiraeng.swing.input.Input;

public class StepInput extends Grade implements Input<Integer>, ActionListener {
	private static final long serialVersionUID = 5964264761989564131L;

	private JSpinner np;

	private JRadioButton pers;

	public StepInput() {
		this(null);
	}

	public StepInput(Grandeza grandeza) {
		ButtonGroup bg = new ButtonGroup();

		this.pers = new JRadioButton("períodos", true);
		pers.addActionListener(this);
		bg.add(pers);
		this.add(pers, 0, 0, 1, 1);

		this.np = new JSpinner(new SpinnerNumberModel(100, 2, 100_000, 1));
		this.add(this.np, 1, 0, 1, 1);

		JRadioButton rb = new JRadioButton("passo");
		rb.addActionListener(this);
		bg.add(rb);
		this.add(rb, 0, 1, 1, 1);

		DoubleInput c;
		if (grandeza != null)
			c = new MedidaInput(0.1, 5, grandeza);
		else
			c = new DoubleInput(0.1, 5);
		c.setEditable(false);
		this.add(c, 1, 1, 1, 1);
	}

	@Override
	public void set(Integer np) {
		this.np.setValue(np);
		if (t0 > 0.)
			((DoubleInput) (this.getComponent(3))).set(this.t0 / np);
	}

	public void setPoints(Integer pts) {
		this.set(pts - 1);
	}

	@Override
	public Integer get() {
		return ((Integer) np.getValue()).intValue();
	}

	private transient double t0 = -1.;

	public void setT(double t) {
		this.t0 = t;
	}

	public int get(double t) {
		this.t0 = t;
		int out = (int) (((DoubleInput) (this.getComponent(3))).get() / this.t0);
		set(out);
		return out + 1;
	}

	public boolean isPoints() {
		return this.pers.isSelected();
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		boolean p = event.getSource().equals(pers);
		np.setEnabled(p); // points
		((DoubleInput) (this.getComponent(3))).setEditable(!p); // step
	}
}