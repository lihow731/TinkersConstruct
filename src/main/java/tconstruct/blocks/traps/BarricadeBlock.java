package tconstruct.blocks.traps;

import mantle.blocks.MantleBlock;
import mantle.common.ComparisonHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tconstruct.client.block.BarricadeRender;
import tconstruct.library.TConstructRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BarricadeBlock extends MantleBlock
{
    Block modelBlock;
    int modelMeta;

    public BarricadeBlock(Block model, int meta)
    {
        super(Material.field_151575_d);
        this.modelBlock = model;
        this.modelMeta = meta;
        func_149711_c(4.0F);
        this.func_149647_a(TConstructRegistry.blockTab);
    }

    public IIcon getIcon (int side, int meta)
    {
        return modelBlock.func_149691_a(2, modelMeta);
    }

    @Override
    public void func_149651_a (IIconRegister par1IconRegister)
    {

    }

    public boolean renderAsNormalBlock ()
    {
        return false;
    }

    public boolean isOpaqueCube ()
    {
        return false;
    }

    public int getRenderType ()
    {
        return BarricadeRender.model;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool (World par1World, int x, int y, int z)
    {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
    }

    public void func_147465_dBoundsBasedOnState (IBlockAccess par1IBlockAccess, int x, int y, int z)
    {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void harvestBlock (World world, EntityPlayer player, int x, int y, int z, int meta)
    {
        if (meta % 4 > 0)
        {
            world.func_147465_d(x, y, z, this, meta - 1, 3);
            dropBlockAsItem_do(world, x, y, z, new ItemStack(this));
        }
        else
        {
            dropBlockAsItem_do(world, x, y, z, new ItemStack(this));
        }
    }

    @Override
    public boolean func_149727_a (World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        ItemStack stack = player.getCurrentEquippedItem();
        if ((stack != null) && (ComparisonHelper.areEquivalent(stack.getItem(), this)) && (!player.isSneaking()))
        {
            int meta = world.getBlockMetadata(x, y, z);
            if (meta % 4 != 3)
            {
                world.func_147465_d(x, y, z, this, meta + 1, 3);
                this.func_149689_a(world, x, y, z, player, stack);
                this.func_149714_e(world, x, y, z, meta);

                Block var9 = this;
                world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, var9.field_149762_H.field_150501_a, (var9.field_149762_H.func_150497_c() + 1.0F) / 2.0F, var9.field_149762_H.func_150494_d() * 0.8F);
                player.swingItem();
                if (!player.capabilities.isCreativeMode)
                    stack.stackSize -= 1;

                return true;
            }
        }
        return false;
    }

    @Override
    public void onBlockExploded (World world, int x, int y, int z, Explosion explosion)
    {
        double distance = (x - explosion.explosionX) + (y - explosion.explosionY) + (z - explosion.explosionZ);
        distance = Math.abs(distance);
        double power = (explosion.explosionSize * 2) / distance;
        int meta = world.getBlockMetadata(x, y, z);
        int trueMeta = meta % 4;
        trueMeta -= power;
        if (trueMeta < 0)
            world.func_147465_d(x, y, z, Blocks.air, 0, 0);
        else
            world.setBlockMetadataWithNotify(x, y, z, (int) (meta - power), 3);
        onBlockDestroyedByExplosion(world, x, y, z, explosion);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean func_149646_a (IBlockAccess world, int x, int y, int z, int side)
    {
        return true;
    }
}