package br.com.pereiraeng.physics.swing;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import br.com.pereiraeng.physics.Grandeza;
import br.com.pereiraeng.physics.Medida;
import br.com.pereiraeng.physics.Unidade;
import br.com.pereiraeng.physics.Unidades;
import br.com.pereiraeng.swing.input.DoubleInput;

/**
 * Classe do objeto gráfico que permite a inserção do valor de uma dada
 * grandeza, com sua respectiva unidade de medida
 * 
 * @author Philipe PEREIRA
 *
 */
public class MedidaInput extends DoubleInput {
	private static final long serialVersionUID = 1L;

	private Unidades units;

	private JLabel unitChooser;

	/**
	 * Construtor do objeto gráfico que permite a entrada de grandezas físicas
	 * 
	 * @param value valor inicial a ser exibido, na forma de uma {@link Medida
	 *              medida} (valor e sua unidade)
	 */
	public MedidaInput(Medida value) {
		this(value, 4, value.getUnit().getGrandeza());
	}

	/**
	 * Construtor do objeto gráfico que permite a entrada de grandezas físicas
	 * 
	 * @param value    valor inicial a ser exibido
	 * @param precisao número de dígitos a serem exibidos e considerados para os
	 *                 números decimais
	 * @param grandeza grandeza física associada à medida (ver
	 *                 {@link Unidade#loadUnitSets(int...)})
	 */
	public MedidaInput(Number value, int precisao, Grandeza grandeza) {
		this(value, precisao, Unidade.getUnits(grandeza));
	}

	/**
	 * Construtor do objeto gráfico que permite a entrada de grandezas físicas
	 * 
	 * @param value    valor inicial a ser exibido
	 * @param precisao número de dígitos a serem exibidos e considerados para os
	 *                 números decimais
	 * @param units    conjunto de unidades da grandeza física associada à medida
	 *                 (ver {@link Const#loadUnitSets(int...)})
	 */
	public MedidaInput(Number value, int precisao, Unidades units) {
		super(precisao);
		this.units = units;

		super.add(this.unitChooser = new JLabel(this.units.get().getSymbol()), BorderLayout.EAST);
		this.unitChooser.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				MedidaInput.this.units.next();
				MedidaInput.this.refreshUnit();
			}
		});

		// preencher campos de texto
		super.innerChange = true;
		if (value instanceof Medida)
			this.set((Medida) value);
		else
			this.set(value.doubleValue());
		super.innerChange = false;

		// criar objeto 'number'
		this.textField2value();
	}

	public void set(Medida m) {
		Unidade u = m.getUnit();
		this.units.set(u);
		u = this.units.get();

		this.unitChooser.setText(u.getSymbol());
		super.set(m.getValue() * u.getValue());
	}

	/**
	 * Função que altera a unidade de medida em que a grandeza é apresentada
	 * 
	 * @param unit unidade em que a grandeza será apresentada
	 */
	public void setUnit(Unidade unit) {
		this.units.set(unit);
		this.refreshUnit();
	}

	/**
	 * Função que altera a unidade de medida em que a grandeza é apresentada
	 * 
	 * @param index índice da unidade dentro da lista
	 */
	public void setUnit(int index) {
		this.units.set(index);
		this.refreshUnit();
	}

	private void refreshUnit() {
		// unidade muda...
		this.unitChooser.setText(this.units.get().getSymbol());

		// ... e o que é exibido também (só o valor que não muda, por isso
		// utiliza-se a função set - que altera somente a caixa de texto)
		super.innerChange = true;
		this.set(getValue());
		super.innerChange = false;
	}

	@Override
	public void setFont(Font font) {
		if (unitChooser != null)
			unitChooser.setFont(font);
		super.setFont(font);
	}

	// ---------------------------- PARSEABLE ----------------------------

	@Override
	protected void setValue(Number number) {
		// para se guardar o valor tratado (aquele pego do campo de texto),
		// antes deve-se considerar a unidade digitada
		super.setValue(number != null ? number.doubleValue() * this.units.get().getValue() : Double.NaN);
	}

	@Override
	protected String value2string(Double value) {
		// o que vai para a caixa de texto
		return super.value2string(value != null ? (double) (value / this.units.get().getValue()) : Double.NaN);
	}
}